# TouchFlip3D 💳🔄
An interactive 3D Flip Card library for Android that supports smooth touch rotation, glossy effects, and shadows.
![JitPack](https://jitpack.io/v/labsisouleimen/TouchFlip3D.svg)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)

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
```
---

## ✨ Features (v1.1 Update)
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
    app:flipDirection="horizontal"
    app:showGlossyEffect="true"
    app:showShadows="true"
    app:enableRotateX="true" 
    app:enableRotateY="true"
    app:autoRotate="false">

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cardBackgroundColor="#F1F1F1" />

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:cardBackgroundColor="#FFFFFF" />

</com.labsisouleimanedev.touchflip3d.RotateView2>
```

---

## ⚙️ XML Attributes
Use these attributes in your layout file to customize the behavior:

| Attribute | Description | Default |
| :--- | :--- | :--- |
| `app:flipDirection` | `0` for Vertical, `1` for Horizontal | `1` |
| `app:showShadows` | Toggle 3D depth shadows | `true` |
| `app:showGlossyEffect` | Toggle shiny light reflection | `false` |
| `app:enableRotateX` | Enable/Disable X-axis rotation | `true` |
| `app:enableRotateY` | Enable/Disable Y-axis rotation | `true` |
| `app:autoRotate` | Full 180° flip on click | `false` |

---

## 💻 Public Methods (Java/Kotlin)
RotateView2 flipView = findViewById(R.id.flipView);

// Reset card to original flat state
flipView.resetRotation();

// Change axis: 0 (Vertical) | 1 (Horizontal)
flipView.setFlipDirection(1);

// Lock or unlock rotation axes
flipView.setEnableRotateX(true);
flipView.setEnableRotateY(true);

## 📱 RecyclerView & ViewPager2 Support
Since **v1.1**, the library handles touch events using `requestDisallowInterceptTouchEvent`. This means you can use the card inside lists or pagers without any scrolling issues. It's plug-and-play!
