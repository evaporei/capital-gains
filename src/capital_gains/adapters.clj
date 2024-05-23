(ns capital-gains.adapters
  (:require [clojure.data.json :as json]))

(defn json->edn
  "Converts JSON String to EDN data structure."
  [json]
  (json/read-str json :key-fn keyword))

(defn edn->json
  "Converts EDN data structure to JSON String."
  [edn]
  (if (nil? edn)
    ""
    (str (json/write-str edn) "\n")))
