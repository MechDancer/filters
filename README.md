# 滤波器

[![Download](https://api.bintray.com/packages/mechdancer/maven/filters/images/download.svg) ](https://bintray.com/mechdancer/maven/filters/_latestVersion)
[![Build Status](https://www.travis-ci.org/MechDancer/filters.svg?branch=master)](https://www.travis-ci.org/MechDancer/filters)

## 概述

所谓滤波器，即是对“波”进行过滤的器件。然而，“波”是一个非常广泛的物理概念，实际上，任何变化的物理量在时空上的序列，即是一个波。因此，所谓滤波器，其实可以泛化到对任何物理量进行处理的工具。

在本工程中，滤波器特指一种从序列到序列的映射。其中，序列中的元素可以是标量，也可以是矢量，也可以是一些更复杂的反映真实世界物理量的结构化数据。由序列到序列的映射，可能导致序列中元素的增加（插值），也可以导致序列列中元素减少（采样），也可以导致序列中元素的值或形式变化。具体来说，对序列的映射必须表达为对序列内元素的映射。这种映射可以是有状态的，也可以是无状态的，但其结果毫无疑问是不可变的。

之所以要建立这一工程，除了阐明滤波器的一致性，更多地是要容纳或简单或复杂的滤波算法。滤波算法是机器人软件最重要的组件之一，因此滤波器的实现和复用，一定程度上就是机器人算法的复用。

本工程旨在提供由纯函数组成的滤波算法，并给用户提供多种风格的接口（面向对象或面向函数）进行滤波函数的扩展和复合。若无特殊说明，则滤波器适用于离散的模拟信号。

## 组件（持续添加）

### 控制环节

* 延时环节
* 差分环节
* 积分环节
* 线性PID控制器

### 典型非线性滤波器

* 限幅器

* 软/硬阈值函数


### 频谱滤波器

* 低通滤波器

### 广义滤波器

* 卡尔曼滤波器

  
## 开始使用

* Gradle
* Maven
* Bintray

您需要将其添加至  [仓库和依赖](https://docs.gradle.org/current/userguide/declaring_dependencies.html) 中。

### Gradle

```groovy
repositories {
    jcenter()
}
dependencies {
    compile 'org.mechdancer:filters:0.1.0'
}
```

### Maven

```xml
<repositories>
   <repository>
     <id>jcenter</id>
     <name>JCenter</name>
     <url>https://jcenter.bintray.com/</url>
   </repository>
</repositories>

<dependency>
  <groupId>org.mechdancer</groupId>
  <artifactId>filters</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

### Bintray

您总可以从 bintray 直接下载 jar：[![Download](https://api.bintray.com/packages/mechdancer/maven/filters/images/download.svg) ](https://bintray.com/mechdancer/maven/filters/_latestVersion)
  