# PDF压缩工具 Android APP

这是一个用于测试的 PDF 压缩工具 Android 项目。

## 当前版本功能

- 选择本地 PDF 文件
- 读取 PDF 文件名和大小
- 原生 Android Java 项目
- GitHub Actions 自动打包 Debug APK
- 无第三方打包平台
- 无第三方水印

## 当前说明

当前 v1.0 是基础框架版，主要目的是先验证：

1. GitHub 仓库源码结构是否正常
2. GitHub Actions 是否可以自动打包 APK
3. 安卓手机是否可以安装测试 APK
4. PDF 文件选择功能是否正常

真实 PDF 压缩、保存、分享功能可以在下一版继续接入。

## 如何自动打包 APK

推送代码到 `main` 分支后，GitHub Actions 会自动运行：

```bash
gradle assembleDebug
```

打包完成后：

1. 打开仓库的 Actions 页面
2. 进入最新一次 Build Android APK 任务
3. 在 Artifacts 下载 `pdf-compressor-debug-apk`
4. 解压后得到 APK 文件

## 包名

```text
com.aihubtools.pdfcompressor
```

## APP 名称

```text
PDF压缩工具
```

## 后续计划

- 接入真实 PDF 压缩算法
- 添加压缩前后大小对比
- 添加保存到 Downloads
- 添加分享压缩后 PDF
- 添加正式 Release 签名与 AAB 打包
