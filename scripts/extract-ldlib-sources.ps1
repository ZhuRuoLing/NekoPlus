# extract-ldlib-sources.ps1
# 从 Gradle 缓存中解压 LDLib2 源码到 run/exported_sources
# 用法: powershell -ExecutionPolicy Bypass -File scripts/extract-ldlib-sources.ps1

param(
    [string]$Version = "2.2.5",
    [string]$Minecraft = "1.21.1"
)

$ErrorActionPreference = "Stop"

$JarName = "ldlib2-neoforge-$Minecraft-$Version-sources.jar"
$GradleRoot = "$env:USERPROFILE\.gradle\caches\modules-2\files-2.1\com.lowdragmc.ldlib2\ldlib2-neoforge-$Minecraft\$Version"
$OutputDir = "$PSScriptRoot\..\run\exported_sources"

Write-Host "Searching for $JarName in $GradleRoot..."

# Find the sources jar in Gradle cache (hash subdirectory varies)
$JarPath = Get-ChildItem -Path $GradleRoot -Recurse -Filter $JarName -File | Select-Object -First 1

if (-not $JarPath) {
    Write-Error "Could not find $JarName in Gradle cache. Run './gradlew dependencies' first."
    exit 1
}

Write-Host "Found: $($JarPath.FullName)"

# Clean old output
if (Test-Path "$OutputDir\com") {
    Remove-Item -Recurse -Force "$OutputDir\com"
}

# Extract
Write-Host "Extracting to $OutputDir..."
Push-Location $OutputDir
jar xf $JarPath.FullName
Pop-Location

Write-Host "Done. Sources extracted to $OutputDir"
