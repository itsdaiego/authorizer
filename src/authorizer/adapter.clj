(ns authorizer.adapter
  (:require [cheshire.core :refer [parse-string]]))

(defn json-to-map
  [input]
  (parse-string input true))
