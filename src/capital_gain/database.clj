(ns capital-gain.database
  (:require [capital-gain.storage :as storage]))

(defn get-state
  "Fetch the database state, defaults keys to 0 if not present."
  [storage]
  {:quantity (storage/get-key storage :quantity 0)
   :weighted-avg (storage/get-key storage :weighted-avg 0)
   :loss (storage/get-key storage :loss 0)})

(defn save-purchase!
  "Updates weighted average and adds new quantity bought."
  [storage stock]
  (storage/set-key! storage :weighted-avg (:weighted-avg stock))
  (storage/update-key! storage :quantity #(+ (or % 0)
                                             (:quantity stock))))

(defn save-loss!
  "Updates loss and subtracts new quantity sold."
  [storage loss stock]
  (storage/set-key! storage :loss loss)
  (storage/update-key! storage :quantity #(- % (:quantity stock))))
