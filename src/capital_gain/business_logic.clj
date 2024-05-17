(ns capital-gain.business-logic)

(defn calculate-weighted-avg
  "new-weighted-avg = ((curr-quantity * curr-weighted-avg) + (new-quantity * new-unit-price)) / (curr-quantity + new-quantity)"
  [curr trade]
  (let [curr-quantity (:quantity curr)
        curr-weighted-avg (:weighted-avg curr)
        new-quantity (:quantity trade)
        new-unit-cost (:unit-cost trade)]
    (/ (+ (* curr-quantity curr-weighted-avg) (* new-quantity new-unit-cost))
       (+ curr-quantity new-quantity))))
