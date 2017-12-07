(ns advent.day6.reallocation)


;; cycle
;; keep track of the number of iterations

;; find bank with the most values
;; if tie pick the first one

;; starting with the next bank
;; redistribute the blocks of this bank with the next and continuing positions

;; stop when a previous state is reached


(def url "./src/advent/day6/day6.txt")

(defn load-file [url]
  (mapv #(Integer/parseInt %) (clojure.string/split (clojure.string/trim (slurp url)) #"\t")))


(defn find-first-max-bank-position-value [v]
  ;; find the bank with the most values
  ;; if there is a tie return the first of the positions
  (let [largest (apply max v)
        pos (.indexOf v largest)]
    [pos largest]))

(defn redistribute-values [v pos amount]
  ;; spread amount evenly starting at the next position from pos
  ;;
  (let [l (count v)
        ]

    )
  )

(defn in-history [h v]
  ;; return true if v is in h
  (> (.indexOf h v) -1))


;; @see https://github.com/plexus/AdventOfCode2017/blob/master/src/advent/day06.clj
(defn redistribute [bs idx]
  (let [cnt (get bs idx)
        wrap #(mod % (count bs))]
    (reduce (fn [bs idx]
              (update bs (wrap idx) inc))
            (assoc bs idx 0)
            (range (inc idx) (inc (+ idx cnt))))))


(defn rebalance [v position value]
  ;; spread the value out starting at position+1
  (redistribute v position)
  )


(defn run-part1 []
  (let [v (load-file url)]
    (loop [v v
           h []
           cnt 0]
      (let [[position value] (find-first-max-bank-position-value v)]
        (if (in-history h v)
            cnt
          (recur (rebalance v position value) (conj h v) (inc cnt))
        )
        )
      )
    )
  )
;; 3156



;; Part 2

;; Figure out how many cycles between the first instance of the cycle value and the end
(defn run-part2 []
  (let [v (load-file url)]
    (loop [v v
           h []
           cnt 0]
      (let [[position value] (find-first-max-bank-position-value v)]
        (if (in-history h v)
          (- cnt (.indexOf h v))
          (recur (rebalance v position value) (conj h v) (inc cnt))
        )
        )
      )
    )
  )
;; 1610

