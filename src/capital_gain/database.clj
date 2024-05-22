(ns capital-gain.database
  (:require [capital-gain.storage :as storage]))

(defn get-state
  "Fetch all previous data from database, aka quantity, unit-price and weighted average.
  Defaults all to zero if db is empty."
  [storage]
  {:quantity (storage/get-key storage :quantity 0)
   :weighted-avg (storage/get-key storage :weighted-avg 0)
   :loss (storage/get-key storage :loss 0)})

(defn save-purchase!
  [storage stock]
  (storage/set-key! storage :weighted-avg (:weighted-avg stock))
  (storage/update-key! storage :quantity #(+ (or % 0)
                                             (:quantity stock))))

(defn save-loss!
  [storage loss stock]
  (storage/set-key! storage :loss loss)
  (storage/update-key! storage :quantity #(- % (:quantity stock))))
