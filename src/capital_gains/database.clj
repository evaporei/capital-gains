(ns capital-gains.database
  (:require [capital-gains.storage :as storage]))

(defn get-state
  "Fetch the database state, defaults keys to 0 if not present."
  [storage]
  {:quantity (storage/get-key storage :quantity 0)
   :weighted-avg (storage/get-key storage :weighted-avg 0)
   :loss (storage/get-key storage :loss 0)})

(defn save-purchase!
  "Updates weighted average and adds new quantity bought."
  [storage trade]
  (storage/set-key! storage :weighted-avg (:weighted-avg trade))
  (storage/update-key! storage :quantity #(+ (or % 0)
                                             (:quantity trade))))

(defn save-loss!
  "Updates loss and subtracts new quantity sold."
  [storage loss trade]
  (storage/set-key! storage :loss loss)
  (storage/update-key! storage :quantity #(- % (:quantity trade))))
