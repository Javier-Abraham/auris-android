# AURIS - Descarga de sonidos desde Freesound.org (licencia CC0)
# Requisito: Registrarte en freesound.org y obtener un API Token
# freesound.org -> My Account -> API Applications -> New Application

param(
    [Parameter(Mandatory=$true)]
    [string]$ApiToken
)

$OutputDir = "$PSScriptRoot\..\app\src\main\res\raw"

# Mapeo: nombre_archivo -> query en inglés (busca loops CC0 de calidad)
$sounds = @{
    "lluvia"       = "rain falling roof loop relaxing"
    "oceano"       = "ocean waves beach loop calm"
    "bosque"       = "forest birds wind ambient loop"
    "tormenta"     = "thunderstorm rain heavy loop"
    "chimenea"     = "fireplace crackling fire loop"
    "viento"       = "wind outdoor ambient loop"
    "ruido_blanco" = "white noise loop"
    "concentracion"= "brown noise focus ambient loop"
}

function Search-FreesoundCC0 {
    param([string]$Query, [string]$Token)

    $url = "https://freesound.org/apiv2/search/text/" +
           "?query=$([uri]::EscapeDataString($Query))" +
           "&filter=license:%22Creative+Commons+0%22+duration:%5B30+TO+300%5D" +
           "&fields=id,name,previews,duration,license" +
           "&page_size=5" +
           "&token=$Token"

    $response = Invoke-RestMethod -Uri $url -Method Get
    return $response.results
}

function Download-Preview {
    param([string]$Url, [string]$OutPath, [string]$Token)

    # Los previews HQ no requieren OAuth - son URLs públicas
    Invoke-WebRequest -Uri $Url -OutFile $OutPath -Headers @{
        "Authorization" = "Token $Token"
    }
}

Write-Host "`nAURIS - Descargando sonidos CC0 de Freesound...`n" -ForegroundColor Cyan

foreach ($entry in $sounds.GetEnumerator()) {
    $nombre = $entry.Key
    $query  = $entry.Value
    $outFile = Join-Path $OutputDir "$nombre.ogg"

    Write-Host "Buscando: $nombre ($query)..." -ForegroundColor Yellow

    try {
        $results = Search-FreesoundCC0 -Query $query -Token $ApiToken

        if ($results.Count -eq 0) {
            Write-Host "  Sin resultados para '$query'" -ForegroundColor Red
            continue
        }

        # Toma el primer resultado con preview ogg disponible
        $sound = $results | Where-Object { $_.previews.'preview-hq-ogg' } | Select-Object -First 1
        if (-not $sound) { $sound = $results[0] }

        $previewUrl = $sound.previews.'preview-hq-ogg'
        if (-not $previewUrl) {
            $previewUrl = $sound.previews.'preview-lq-ogg'
        }

        Write-Host "  Encontrado: '$($sound.name)' (ID: $($sound.id), $([math]::Round($sound.duration))s)" -ForegroundColor Green
        Write-Host "  Licencia: $($sound.license)" -ForegroundColor Green

        Download-Preview -Url $previewUrl -OutPath $outFile -Token $ApiToken

        $size = [math]::Round((Get-Item $outFile).Length / 1KB, 1)
        Write-Host "  Guardado: $nombre.ogg ($size KB)`n" -ForegroundColor Cyan

    } catch {
        Write-Host "  ERROR: $_" -ForegroundColor Red
    }
}

Write-Host "`nListo! Verificando archivos:" -ForegroundColor Green
Get-ChildItem $OutputDir | Select-Object Name, @{N='KB';E={[math]::Round($_.Length/1KB,1)}} | Format-Table
Write-Host "`nPROXIMO PASO: Actualizar SoundRepository.kt con rawResId = R.raw.xxx" -ForegroundColor Magenta
