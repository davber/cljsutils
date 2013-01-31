cljsutils
=========

Some utils for ClojureScript development, such as proper JSON
marshalling.

There are two files you should link to from your ClojureScript
project:

* `cljs/utils.clj` - the main utils library
* `cljs-macros/macros/utils.clj` - some macro facilities helping out

**NB**: the version of ClojureScript used is that of
https://github.com/davber/clojurescript/tree/crossing, since the
regular version forces one to use a `cljs` suffix
