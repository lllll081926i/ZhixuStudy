param (
    [switch]$SkipBuild,
    [switch]$Logcat
)

$ErrorActionPreference = 'Stop'

# 1. 注入 D 盘环境
$env:JAVA_HOME = 'D:\Android\jdk-17'
$env:GRADLE_USER_HOME = 'D:\Android\.gradle'
$env:ANDROID_HOME = 'D:\Android\Sdk'
$ADB = 'D:\Android\Sdk\platform-tools\adb.exe'

Write-Host "`n>>> [1/4] Checking connected Android devices or emulators..." -ForegroundColor Cyan

# 尝试连接常见本地模拟器端口 (MuMu / 夜神 / 雷电 / 标准)
$null = & $ADB connect 127.0.0.1:16384 2>&1
$null = & $ADB connect 127.0.0.1:7555 2>&1
$null = & $ADB connect 127.0.0.1:5555 2>&1

$devicesOutput = & $ADB devices
$onlineDevices = ($devicesOutput -split "`r?`n" | Where-Object { $_ -match "\bdevice\b" -and $_ -notmatch "List of" })

if (-not $onlineDevices) {
    Write-Host "`n[!] No online Android device or emulator detected!" -ForegroundColor Yellow
    Write-Host "Please do one of the following and run again:" -ForegroundColor Yellow
    Write-Host "  1. Start an Android emulator (e.g., MuMu 12, LDPlayer, Android Studio AVD);" -ForegroundColor Yellow
    Write-Host "  2. Connect your Android phone via USB and enable USB Debugging." -ForegroundColor Yellow
    exit 1
}

Write-Host "Online devices found:`n$($onlineDevices -join "`n")" -ForegroundColor Green

# 2. 编译打包
$apkPath = "$PSScriptRoot\app\build\outputs\apk\debug\app-debug.apk"
if (-not $SkipBuild) {
    Write-Host "`n>>> [2/4] Building APK via Gradle (assembleDebug)..." -ForegroundColor Cyan
    & "$PSScriptRoot\gradlew.bat" assembleDebug
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[x] Build failed! Check errors above." -ForegroundColor Red
        exit $LASTEXITCODE
    }
} else {
    Write-Host "`n>>> [2/4] Skipping build, using existing APK..." -ForegroundColor Cyan
}

if (-not (Test-Path $apkPath)) {
    Write-Host "[x] APK file not found at: $apkPath" -ForegroundColor Red
    exit 1
}

# 3. 安装到设备
$apkSizeMB = [math]::Round((Get-Item $apkPath).Length / 1MB, 2)
Write-Host "`n>>> [3/4] Installing APK via ADB ($apkSizeMB MB)..." -ForegroundColor Cyan
& $ADB install -r -t $apkPath
if ($LASTEXITCODE -ne 0) {
    Write-Host "[x] Installation failed!" -ForegroundColor Red
    exit $LASTEXITCODE
}

# 4. 启动主界面
$packageName = "com.aistudio.zhixustudy.kxzqmp"
$activityName = "com.example.MainActivity"
Write-Host "`n>>> [4/4] Starting ZhiXu Study main activity ($packageName/$activityName)..." -ForegroundColor Cyan
& $ADB shell am start -n "$packageName/$activityName"

Write-Host "`n=======================================================" -ForegroundColor Green
Write-Host "Done! ZhiXu Study is running on the device." -ForegroundColor Green
Write-Host "=======================================================`n" -ForegroundColor Green

if ($Logcat) {
    Write-Host ">>> Streaming app logcat (Press Ctrl+C to stop)..." -ForegroundColor Cyan
    & $ADB logcat -v time --pid=(& $ADB shell pidof -s $packageName)
}
