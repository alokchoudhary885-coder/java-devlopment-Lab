# Setup Maven Wrapper / Portable Maven for ShopSphere
$ErrorActionPreference = "Stop"

$projectRoot = $PSScriptRoot
$wrapperDir = Join-Path $projectRoot ".mvn\wrapper"
$wrapperJar = Join-Path $wrapperDir "maven-wrapper.jar"
$wrapperJarUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

Write-Host "Checking Maven Wrapper jar in $wrapperDir..."
if (-not (Test-Path $wrapperJar)) {
    Write-Host "Downloading maven-wrapper.jar from $wrapperJarUrl..."
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 -bor [Net.SecurityProtocolType]::Tls13
        Invoke-WebRequest -Uri $wrapperJarUrl -OutFile $wrapperJar -UseBasicParsing
        Write-Host "maven-wrapper.jar successfully downloaded."
    } catch {
        Write-Warning "Could not download wrapper jar directly: $_"
    }
} else {
    Write-Host "maven-wrapper.jar already present."
}

# Also ensure portable Maven binary is cached in tools if needed
$toolsDir = Join-Path $projectRoot ".tools\maven"
$mvnBin = Join-Path $toolsDir "apache-maven-3.9.9\bin\mvn.cmd"

if (-not (Test-Path $mvnBin)) {
    New-Item -ItemType Directory -Force -Path (Join-Path $projectRoot ".tools") | Out-Null
    $zipPath = Join-Path $projectRoot ".tools\apache-maven-3.9.9-bin.zip"
    $mvnUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip"
    
    Write-Host "Downloading standalone Maven 3.9.9 archive..."
    try {
        Invoke-WebRequest -Uri $mvnUrl -OutFile $zipPath -UseBasicParsing
        Write-Host "Extracting Maven..."
        Expand-Archive -Path $zipPath -DestinationPath $toolsDir -Force
        Remove-Item $zipPath -Force
        Write-Host "Maven 3.9.9 successfully staged in .tools/maven"
    } catch {
        Write-Warning "Optional portable maven download failed: $_"
    }
}

Write-Host "Maven setup check completed."
