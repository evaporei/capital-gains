(ns capital-gains.controller
  (:require [capital-gains.business-logic :as logic]
            [capital-gains.adapters :as adapters]
            [capital-gains.database :as db]))

(defn buy-stocks
  "Buy stocks controller."
  [storage trade]
  (let [curr-state (db/get-state storage)
        new-weighted-avg (logic/calculate-weighted-avg curr-state trade)]
    (db/save-purchase! storage {:quantity (:quantity trade)
                                :weighted-avg new-weighted-avg})
    {:tax 0}))

(defn sell-stocks
  "Sell stocks controller."
  [storage trade]
  (let [{weighted-avg :weighted-avg
         loss :loss} (db/get-state storage)
        new-cost (:unit-cost trade)
        {tax :tax
         loss :new-loss} (logic/calculate-loss weighted-avg
                                               new-cost
                                               loss
                                               (:quantity trade))]
    (db/save-loss! storage loss trade)
    {:tax tax}))

(defn execute-controller
  "Executes controller with storage and user input."
  [storage controller-and-input]
  (let [[controller input-data] controller-and-input]
    (controller storage input-data)))

(defn routing
  "Routes to appropriate controller based of map key."
  [trade]
  (case (:operation trade)
    "buy" [buy-stocks trade]
    "sell" [sell-stocks trade]
    [(constantly nil) trade]))

(defn controller
  "Adapts and routes the user input to the correct controller, and executes it.
  It returns a JSON string with the result."
  [storage input]
  (->> input
       adapters/json->edn
       (map (comp (partial execute-controller storage) routing))
       adapters/edn->json))
