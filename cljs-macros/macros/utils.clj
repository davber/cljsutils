(ns macros.utils
"This module provides some common macros and definitions"
	(:require [clojure.string :as string]))


;; TODO: for the "meta macros" below we have to manually expand each
;; nested macro form
;; here, since as soon as the top form is not a macro form.
;; we leave this code behind and enter ClojureScript land.
;; Investigate!

(defmacro macro-map
"Maps a macro on each list form passed as remaining arguments.
 The macro is
 just injected as the first argument of each list, and then
 all the resulting lists are combined into a do form."
  [macro & exprs]
  `(do ~@(map (partial cons macro) exprs)))

(defmacro macro-map-list
"A variant of macro-map which instead of a macro as first
 argument expects a macro form, which will be concatenated
 with each argument list.
 This allows the specification of a partial macro form, with
 some parameters set."
  [macro-form & exprs]
  `(do ~@(map (partial concat macro-form) exprs)))

(defmacro interns
"Works like the binary intern macro, but allowing multiple
 symbols to be internalized at once."
 [ns & symbols]
 `(macro-map-list (intern ~ns) ~@(map list symbols)))

;; TODO: put this definition in one place instead of duplicating it
(defn wordify
"Splits a string or symbol into a sequence of lower-case words"
	[lexeme]
	(string/split (name lexeme) #"[_-]"))

(defmacro if-catch
  [cond & body]
  (if cond
    `(try ~@body (catch js/Error ex#)))
    `(do ~@body))

(defmacro opt-parse-json
"A JSON parser, which does go from either a JSON string or an already
 existing (map) object."
  [obj]
  `(if (string? ~obj) (parse-json ~obj) ~obj))


(defmacro parse-json
  "Will parse a JSON string into an object, including
transforming it from a JS data structure to a regular
Clojure structure, if needed."
  [json-str & {:keys [use-keys]}]
  (let [obj `(~'utils/cljify (.parse (? JSON) ~json-str))]
    (if use-keys `(walk/keywordize-keys ~obj) `~obj)))

(defmacro output-json
  [obj]
  `(.stringify (? JSON) (~'utils/jsify ~obj)))
