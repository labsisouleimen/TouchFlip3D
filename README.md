# TouchFlip3D 💳🔄
An interactive 3D Flip Card library for Android that supports smooth touch rotation, glossy effects, and shadows.

<p align="center">
  <img src="https://github.com/user-attachments/assets/8f33362f-2d24-4d14-ac08-a1f1fc2b5d7e" width="320">
</p>

## 🚀 Installation (Latest Version: v1.1)

Add it in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url '[https://jitpack.io](https://jitpack.io)' }
    }
}
Add the dependency to your app/build.gradle:
dependencies {
    implementation 'com.github.labsisouleimen:TouchFlip3D:v1.1'
}
✨ Features (v1.1 Update)
Smooth 3D Rotation: Interactive touch-based card flipping.

Glossy Effect: Dynamic light reflections while moving.

Shadow System: Realistic 3D depth shadows.

RecyclerView Ready: Fixed touch interception for seamless scrolling.
## 🛠 XML Usage
You must place exactly **two** children inside `RotateView2`:
1. The first child is the **Back Side**.
2. The second child is the **Front Side**.

```xml
<com.labsisouleimanedev.touchflip3d.RotateView2
    android:id="@+id/flipView"
    android:layout_width="320dp"
    android:layout_height="220dp"
    app:showGlossyEffect="true"
    app:showShadows="true"
    app:flipDirection="horizontal">

    <androidx.cardview.widget.CardView ... />

    <androidx.cardview.widget.CardView ... />

</com.labsisouleimanedev.touchflip3d.RotateView2>
## ⚙️ Attributes

| Attribute | Description | Default |
|-----------|-------------|---------|
| `app:flipDirection` | `horizontal` (1) or `vertical` (0) | `horizontal` |
| `app:showShadows` | Enable/Disable 3D shadows during movement | `true` |
| `app:showGlossyEffect` | Enable/Disable the shiny light effect | `false` |
| `app:autoRotate` | If true, the card flips 180° when clicked | `false` |
| `app:enableRotateX` | Allow/Lock rotation on X axis | `true` |
| `app:enableRotateY` | Allow/Lock rotation on Y axis | `true` |
## 📱 RecyclerView & ViewPager2 Support
Since **v1.1**, the library handles touch events using `requestDisallowInterceptTouchEvent`. This means you can use the card inside lists or pagers without any scrolling issues. It's plug-and-play!
