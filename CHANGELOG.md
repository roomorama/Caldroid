3.0.1
=====

- Fix issue #367: Caldroid now saves SQUARE_TEXT_VIEW_CELL properly during rotation.

3.0.0
=====

- Removed "setBackgroundResource*" methods and replaced by "setBackgroundDrawable*" methods. This is more flexible and allow client to create dynamic drawable instead of having to create static resources.

- Fixed crashes in Android 4x, where ContextThemeWrapper doesn't apply theme correctly.
