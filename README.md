# CheckImgFormat 检查图片格式

[![](https://jitpack.io/v/by-syk/CheckImgFormat.svg)](https://jitpack.io/#by-syk/CheckImgFormat)
[![](https://img.shields.io/badge/Download%20aar-1.0.2-brightgreen.svg)](out/checkimgformat-1.0.2.aar)


对图片文件进行常见格式检查：「**.jpg**」、「**.png**」、「**.gif**」、「**.bmp**」

首先会检查文件后缀；然后判断魔术数字，即检查文件数据流的前几个字节。参考冯立彬的博客：[使用JAVA如何对图片进行格式检查以及安全检查处理](http://blog.csdn.net/fenglibing/article/details/7728275)

> 注：这些检查手段非常基础，请勿用于安全要求场景，但可作为前期判断。


### 添加库到项目

此项目已托管到 [**JitPack.io**](https://jitpack.io/)。

**第一步** 在项目根`build.gradle`中添加:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**第二步** 在模块中添加依赖：

```
dependencies {
    compile 'com.github.by-syk:CheckImgFormat:1.0.2'
}
```


### 如何使用

```java
// FORMAT_JPG, FORMAT_PNG, FORMAT_GIF, FORMAT_BMP, FORMAT_UNDEFINED
String format = CheckImgFormat.get(imgFile);
String format = CheckImgFormat.get(imgUri);
String format = CheckImgFormat.get(imgInputStream);
```

```java
boolean isPng = CheckImgFormat.is(CheckImgFormat.FORMAT_PNG, imgFile);
```


### DEMO APP

[下载](out/CheckImgFormatSample.apk)


### License

    Copyright 2017 By_syk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


*Copyright &#169; 2017 By_syk. All rights reserved.*