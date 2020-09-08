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

![bage](https://jitpack.io/v/ijkzen/ToggleButton.svg)

```groovy
dependencies {
    ...
    implementation 'com.github.ijkzen:ToggleButton:<latest_version>'
}
```

### Practice

0. Example

```xml
<com.github.ijkzen.ToggleButton
        android:id="@+id/toggle2"
        android:layout_centerHorizontal="true"
        android:layout_below="@id/toggle"
        android:layout_marginTop="30dp"
        android:layout_width="120dp"
        android:layout_height="70dp"/>
```





1. Use `attributes` on xml

![attrs](./preview/attrs.png)

2. Set `attributes` programmatically

```kotlin
    //  set button color

    fun setCheckedBackgroundColor(@ColorRes color: Int) {
        mCheckedBackgroundColor = color
        invalidate()
    }

    fun setUncheckedBackgroundColor(@ColorRes color: Int) {
        mUncheckedBackgroundColor = color
        invalidate()
    }

    fun setCheckedRoundColor(@ColorRes color: Int) {
        mCheckedRoundColor = color
        invalidate()
    }

    fun setUncheckedRoundColor(@ColorRes color: Int) {
        mUncheckedRoundColor = color
        invalidate()
    }

    fun setDisableCheckedBackgroundColor(@ColorRes color: Int) {
        mDisableCheckedBackgroundColor = color
        invalidate()
    }

    fun setDisableUncheckBackgroundColor(@ColorRes color: Int) {
        mDisableUncheckedBackgroundColor = color
        invalidate()
    }

    fun setDisableCheckedRoundColor(@ColorRes color: Int) {
        mDisableCheckedRoundColor = color
        invalidate()
    }

    fun setDisableUncheckedRoundColor(@ColorRes color: Int) {
        mDisableUncheckedRoundColor = color
        invalidate()
    }

//  set button status

    fun setChecked(checked: Boolean) {
        if (checked != mIsChecked) {
            mIsChecked = !mIsChecked
            mIsChanged = true
            mTouchUpTime = System.currentTimeMillis()
            invalidate()
        }
    }

    fun isChecked() = mIsChecked

    fun toggle() {
        if (isButtonEnabled()) {
            setChecked(!mIsChecked)
        }
    }

    fun setButtonEnabled(enabled: Boolean) {
        if (mIsEnabled != enabled) {
            mIsEnabled = enabled
            invalidate()
        }
    }

    fun isButtonEnabled() = mIsEnabled

    fun setDuration(duration: Int) {
        mDuration = if (duration < DEFAULT_DURATION) {
            DEFAULT_DURATION
        } else {
            duration
        }
    }
```

### More

If you have any questions, please ask me [here](https://github.com/ijkzen/ToggleButton/issues)