#Android 图片模糊 性能总结

模糊一张图片不难，各种算法也很多:高斯、均值、矩阵等等，但是在移动端需要考虑APP的使用体验，对于性能的要求还是很高的。所以在安卓中纯粹使用Java去实现肯定是最坑爹的做法，我们有另外两种方式可以选择：<a target="_blank" href="http://developer.android.com/ndk/index.html">NDK</a> or <a target="_blank" href="http://developer.android.com/guide/topics/renderscript/compute.html">Renderscript</a>


----------


Renderscript的优缺点
----------------
Renderscript，原则上讲应该是安卓上的最佳选择。它自带了模糊图片的算法，在计算速度上可以说已经非常出色了。但是……

**优点**：

1. 计算速度较快，主要是对CPU的利用比较全面
2. 配置工程方便（相对NDK）
3. 在不需要自定义算法的情况下使用还是很方便的，只需要写一段java代码就可以完成操作

**缺点**：

1. 学习成本高（如果要自定义一些算法的话）
2. 移植代码基本不可能
3. 对安卓版本要求较高，比如模糊图片的算法就必须要API17以上 
4. 自带的模糊算法速度虽快但效果不尽如人意！

Renderscript的模糊用的是什么算法我不清楚，但是出来的效果不太理想！具体效果往后面看，都丁达尔效应了！


----------


NDK的优缺点
-------
NDK在讲求性能的应用中肯定是不能少的，不过我自己用的并不多！所以如果有说的不对请见谅！

**优点**：

1. 用C或C++，肯定很多人都会（感觉又回到在学校A题的时候了）。
2. 可应用范围比较广泛， 代码移植方便，有很多现成的库可以拉过来用
3. 在进行高复杂度的计算时甩Java好几条街


**缺点**：

1. 我自己写了半天算法完全追不上Renderscript的速度，所以需要有深厚的功力才能很好的发挥它的作用
2. Eclipse上配置繁琐，Stusio中稍微简化了点
3. 代码调试查错比较麻烦，可能还是跟我的水平有关吧！



----------


效果对比
----
我用NDK写了两个算法(一维高斯和均值)，然后跟Renderscript自带的模糊做个对比。理论上高斯模糊的效果应是最好的，但是我用了一维高斯叠加的算法所以效果不理想！但是二维高斯又太耗时，递归高斯我又不会！只能这样了 - - 

想看代码的<a target="_blank" href="https://github.com/tyzlmjj/AndroidUI/tree/master/GaussianBlur">点我</a>

- 原图：

![这里写图片描述](http://img.blog.csdn.net/20160307232253075)

- 模糊后的图
右键在链接中打开可以看大图

![这里写图片描述](http://img.blog.csdn.net/20160307232320940)

----------


总结
--

1. 将原图缩小后模糊，然后再放大显示。这样可以节省很多时间。推荐缩放到100像素左右
2. 需要渐变效果时可以用两个图层叠加改变透明度的方式。这样多占点内存，但是只需要计算一次
3. 我个人感觉最理想的应该还是用NDK来实现，可惜本人C只学了点皮毛，写不好啊啊啊
4. 算法很重要！

----------


参考资料
----

均值模糊算法：http://blog.ivank.net/fastest-gaussian-blur.html

二维高斯模糊算法：http://www.ruanyifeng.com/blog/2012/11/gaussian_blur.html

一维高斯模糊算法：http://www.cnblogs.com/hoodlum1980/p/4528486.html

Android NDK配置：http://blog.csdn.net/tyzlmjj/article/details/50725281

RenderScript 配置和使用：http://blog.csdn.net/tyzlmjj/article/details/50747173
