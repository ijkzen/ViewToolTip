![bage](https://jitpack.io/v/ijkzen/ViewToolTip.svg) ![week_download](https://jitpack.io/v/ijkzen/ViewToolTip/week.svg) ![month_download](https://jitpack.io/v/ijkzen/ViewToolTip/month.svg)
### Preview

<div align=center><img style="height:400px" src="./preview/preview.gif"/></div>

### Usage

1. Add it in your **root**  `build.gradle` at the end of repositories

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add the dependency

![bage](https://jitpack.io/v/ijkzen/ViewToolTip.svg)

```groovy
dependencies {
    ...
    implementation 'com.github.ijkzen:ViewToolTip:<latest_version>'
}
```

### Practice

0. Example

```kotlin
val tip1 = ViewToolTip.on(mBinding.text1)
            .customView(binding1.root)
            .gravity(TipGravity.BOTTOM)

tip1.show()
```





1. Configure `ViewToolTip` by chaining commands

```kotlin
interface ToolTipConfiguration {
    fun customView(contentView: View): ViewToolTip

    // For gravity top or bottom, PopupWindow will match screen width.
    fun widthMatchParent(match: Boolean): ViewToolTip

    // PopupWindow show around targetView, left, top, right or bottom.
    fun gravity(gravity: TipGravity): ViewToolTip

    // width for arrow pointing to the targetView.
    fun arrowWidth(width: Int): ViewToolTip

    // height for arrow pointing to the targetView.
    fun arrowHeight(height: Int): ViewToolTip

    // color for arrow pointing to the targetView
    fun arrowColor(color: Int): ViewToolTip

    // set background for popupWindow if not customView
    fun background(background: GradientDrawable): ViewToolTip

    // set background color for popupWindow if not customView
    fun backgroundColor(color: Int): ViewToolTip

    // set background radius for popupWindow if not customView
    fun backgroundRadius(radius: Int): ViewToolTip

    // set text for popupWindow if not customView
    fun text(text: CharSequence): ViewToolTip

    // set text color if not customView
    fun textColor(color: Int): ViewToolTip

    // set text size if not custmView
    fun textSize(size: Float): ViewToolTip

    //  set text alignment if not custmView
    fun textAlign(align: Int): ViewToolTip

    // show grey background for popupWindow or not
    fun isShowMaskBackground(show: Boolean): ViewToolTip

    // dismiss popupWindow when clicking grey background 
    fun isAllowHideByClickMask(allow: Boolean): ViewToolTip

    // dimiss popupWindow when clicking popupWindow
    fun isAllowHideByClickTip(allow: Boolean): ViewToolTip

    // dimiss popupWindow after sometime
    fun isAutoHide(auto: Boolean): ViewToolTip

    // sometime above
    fun displayTime(duration: Long): ViewToolTip

    // set tag for popupWindow
    fun setTag(tag: String): ViewToolTip

    // if tag == mTag, popupWindow will keep showing, otherwise, popupWindow will dismiss
    fun notify(tag: String?)
}
```

### More

If you have any questions, please ask me [here](https://github.com/ijkzen/ViewToolTip/issues)