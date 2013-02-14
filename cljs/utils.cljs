(ns utils
  (:require [clojure.string :as string]
            [clojure.walk :as walk])
  (:require-macros
            [macros.utils :as umacros]))

(declare wordify cljify jsify)

(defn ^:export camelize
  "Camelize a string - or a symbol or keyword - by replacing dashes and underscores with word
 boundaries. It will always generate a string.
 There is an optional parameter indicating whether the first letter
 should be capitalized (i.e., a Pascal Camel...)"
  [lexeme & { :keys [capitalize-first] :or { capitalize-first false}}]
  (let [[first & rest] (utils/wordify lexeme)
        cap-first (if capitalize-first (string/capitalize first) first)
        cap-rest (map string/capitalize rest)]
    (string/join (cons cap-first cap-rest))))

(defn ^:export hyphenize
  "Hyphenizes a string by dividing it into lower-case words and joining with a
 hyphen. It supports symbols as well."
  [lexeme]
  (string/join "-" (wordify lexeme)))

;; TODO: put this definition in one place instead of duplicating it
(defn ^:export wordify
  "Splits a string or symbol into a sequence of lower-case words"
  [lexeme]
  (string/split (name lexeme) #"[_-]"))

(defn ^:export log
  "Log using the JS console or trace. The first argument is the log level."
  [level & msgs]
  (let [info (apply str `(~level ": " ~@msgs))]
    (umacros/if-catch true (.log js/console info))))

(defn- jsify-outer [value]
  (cond (seq? value) (do (print (str "jsify-outer with seq " value))
                       (apply array value))
        (map? value)
        (let [jsobj (js-obj)]
          (doseq [[k v] value]
            (aset jsobj k v))
          jsobj)
        :else value))

;; The clj->js function is copied from http://mmcgrana.github.com/2011/09/clojurescript-nodejs.html
(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings."
  [x]
  (cond
    (string? x) x
    (keyword? x) (name x)
    (map? x) (.-strobj (reduce (fn [m [k v]]
               (assoc m (clj->js k) (clj->js v))) {} x))
    (coll? x) (apply array (map clj->js x))
    :else x))


(defn ^:export jsify [value]
  ;; (print (str "utils/jsify of value " value))
  (let [jsobj (clj->js (walk/stringify-keys value))]
    ;; (.log js/console jsobj)
    jsobj))

(defn ^:export cljify [value]
  ;; (print (str "utils/jcljify of value " value))
  (-> value js->clj walk/keywordize-keys))

;; The following UUID generator is from
;; http://catamorphic.wordpress.com/2012/03/02/generating-a-random-uuid-in-clojurescript/
(defn ^:export uuid
  "returns a type 4 random UUID: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx"
  []
  (let [r (repeatedly 30 (fn [] (.toString (rand-int 16) 16)))]
    (apply str (concat (take 8 r) ["-"]
                       (take 4 (drop 8 r)) ["-4"]
                       (take 3 (drop 12 r)) ["-"]
                       [(.toString  (bit-or 0x8 (bit-and 0x3 (rand-int 15))) 16)]
                       (take 3 (drop 15 r)) ["-"]
                       (take 12 (drop 18 r))))))
