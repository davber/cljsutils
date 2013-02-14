cljsutils
=========

Some utils for ClojureScript development, such as proper JSON
marshalling.

There are two files you should include in the source paths of your ClojureScript project:

* `cljs/utils.cljs` - the main utils library
* `cljs-macros/macros/utils.clj` - some macro facilities helping out

**NOTE**: using Leiningen, you just add the `cljs` path to the `cljsbuild`-specific source path and `cljs-macros` to the general source path.

