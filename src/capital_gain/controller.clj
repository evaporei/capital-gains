(ns capital-gain.controller
  (:require [capital-gain.business-logic :as logic]
            [capital-gain.adapters :as adapters]
            [capital-gain.database :as db]))

(defn buy-stocks
  "Buy stocks"
  [storage trade]
  (let [curr (db/get-all storage)
        new-weighted-avg (logic/calculate-weighted-avg curr trade)]
    (db/save-stock! storage {:quantity (:quantity trade)
                             :weighted-avg new-weighted-avg})
    {:tax 0}))

(defn sell-stocks
  "Sell stocks"
  [storage trade]
  (let [{weighted-avg :weighted-avg
         loss :loss} (db/get-all storage)
        new-cost (:unit-cost trade)
        {tax :tax
         loss :new-loss} (logic/sell-stock weighted-avg
                                           new-cost
                                           loss
                                           (:quantity trade))]
    (db/sell-stock! storage loss trade)
    {:tax tax}))

;; review
(defn execute-controller
  "Executes controller with storage and user input."
  [storage controller-and-input]
  (let [[controller input-data] controller-and-input
        res (controller storage input-data)]
      ;; (println (deref (:storage storage)))
      res))

(defn routing
  "Routes to appropriate controller based of map key."
  [trade]
  (case (:operation trade)
    "buy" [buy-stocks trade]
    "sell" [sell-stocks trade]
    [(constantly nil) trade]))

(defn controller
  "Adapts and routes the user input to the correct controller, and executes it.
  It returns the JSON string just as it received."
  [storage input]
  (->> input
       adapters/json->edn
       ;; ewww
       (map (comp (partial execute-controller storage) routing))
       adapters/edn->json))
