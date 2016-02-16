3.0.0
=====

- Removed "setBackgroundResource*" methods and replaced by "setBackgroundDrawable*" methods. This is more flexible and allow client to create dynamic drawable instead of having to create static resources.

- Fixed crashes in Android 4x, where ContextThemeWrapper doesn't apply theme correctly.
