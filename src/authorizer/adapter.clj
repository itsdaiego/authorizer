(ns authorizer.adapter
  (:require [cheshire.core :refer [parse-string generate-string]]))

(defn json-to-hmap
  [input]
  (parse-string input true))

(defn hmap-to-json
  [input]
  (generate-string input))
