(ns capital-gain.database
  (:require [capital-gain.storage :as storage]))

(defn get-all
  "Fetch all previous data from database, aka quantity, unit-price and weighted average.
  Defaults all to zero if db is empty."
  [storage]
  {:quantity (storage/get-key-with-default storage :quantity 0)
   :weighted-avg (storage/get-key-with-default storage :weighted-avg 0)})

(defn save-stock!
  [storage stock]
  (doseq [[k v] stock]
    (storage/insert-key! storage k v)))
