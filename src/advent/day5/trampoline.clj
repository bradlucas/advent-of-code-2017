(ns advent.day5.trampoline)


;; 0 3 0 1 -3
;; 1 3 0 1 -3
;; 2 (3) 0 1 -3
;; 2 4 0 1 (-3)
;; 2 (4) 0 1 -2 => 
;; 2 5 0 1 -2 =>

(def url "/Users/Brad/work/advent-of-code-2017/src/advent/day5/day5.txt")



;; read file and convert each line into an element in an array
(def v [0 3  0  1  -3])

;; loop over array and 'follow' the directions while updating each step
(defn process-instructions [v]
  (let [len (count v)]
    (loop [ary v     ;; current version of the array
           pos 0     ;; 0-based current position
           cnt 1     ;; number of jumps so far
           ]
      ;; (println ary pos cnt)
      (let [current-value (nth ary pos)       ;; value at the current position
            new-value (inc current-value)     ;; next value for position
            next-position (+ pos current-value)
            ]
        ;; (println current-value new-value next-position)
      (if (or (< next-position -1) (>= next-position len))
        cnt
        (recur (assoc ary pos new-value) next-position (inc cnt)))))))
    

;; read value and increase value by 1
;; jump to value
;; if out of bounds return number of steps taken so far

(defn run1 [url]
  (with-open [rdr (clojure.java.io/reader url)]
    (process-instructions (mapv #(Integer/parseInt %) (line-seq rdr)))))
;; 358309      

(defn run-part1 []
  (run1 url))
  

;; Part 2

;; if offset was 3 or more then decrease by 1
;; else increase by 1 as bewfore



(defn process-instructions2 [v]
  (let [len (count v)]
    (loop [ary v     ;; current version of the array
           pos 0     ;; 0-based current position
           cnt 1     ;; number of jumps so far
           ]
      ;; (println ary pos cnt)
      (let [current-value (nth ary pos)       ;; value at the current position
            new-value (if (> current-value 2) (dec current-value) (inc current-value))     ;; next value for position
            next-position (+ pos current-value)
            ]
        ;; (println current-value new-value next-position)
      (if (or (< next-position -1) (>= next-position len))
        cnt
        (recur (assoc ary pos new-value) next-position (inc cnt)))))))

(defn run2 [url]
  (with-open [rdr (clojure.java.io/reader url)]
    (process-instructions2 (mapv #(Integer/parseInt %) (line-seq rdr)))))
;; 28178177

(defn run-part2 []
  (run2 url))
