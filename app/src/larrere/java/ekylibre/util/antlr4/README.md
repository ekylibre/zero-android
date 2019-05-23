# Generating ANTLR4 parser

- Install ANTLR4 plugin in Android Studio
- Configure ANTLR4 with right-click on grammar file and set following fields :
    - _Output directory where all output is generated_ <your_android_project_base_path>/zero-android/app/src/larrere/java
    - _Package/namespace for the generated code_ ekylibre.util.antlr4
- Tick only _Generate parse tree listener (default)_