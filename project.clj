(defproject utilstest "0.0.1"
  :description "Just an empty project to compile the cljsutils library"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :plugins [[lein-cljsbuild "0.3.0"]]
  :source-paths ["cljs-macros"]
  :cljsbuild {
    :builds [
      {:source-paths ["cljs"]
       :compiler {
       	:output-to "target/main.js"
       	:optimizations :whitespace
       	:pretty-print true
        }}]
  })
