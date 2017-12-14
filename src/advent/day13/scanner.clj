(ns advent.day13.scanner
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Input
;; depth: range

;; 0: 3
;; 1: 2
;; 4: 4
;; 6: 4

;;  0   1   2   3   4   5   6
;; [ ] [ ] ... ... [ ] ... [ ]
;; [ ] [ ]         [ ]     [ ]
;; [ ]             [ ]     [ ]
;;                 [ ]     [ ]

(def s "0: 3
1: 2
4: 4
6: 4")

(def url "./src/advent/day13/day13.txt")

(defn process-lines [lines]
  (map (fn [l]
         (apply hash-map (map #(Integer/parseInt (str/trim %)) (str/split l #":")))) lines))

(defn load-data-str [s]
  ;; Input:  "0: 3\n1: 2\n4: 4\n6: 4"
  ;; Output: {0 3, 1 2, 4 4, 6 4}
  (into (sorted-map) (-> s
                         clojure.string/trim
                         (clojure.string/split #"\n")
                         process-lines)))

(defn load-data
  ([] (load-data-str (slurp url)))
  ([s] (load-data-str s)))
;; {0 3, 1 2, 2 4, 4 6, 6 4, 8 6, 10 5, 12 6, 14 8, 16 8, 18 8, 20 6, 22 12, 24 8, 26 8, 28 10, 30 9, 32 12, 34 8, 36 12, 38 12, 40 12, 42 14, 44 14, 46 12, 48 12, 50 12, 52 12, 54 14, 56 12, 58 14, 60 14, 62 14, 64 14, 70 10, 72 14, 74 14, 76 14, 78 14, 82 14, 86 17, 88 18, 96 26}



;; bhauman
;; @see ehttps://github.com/bhauman/advent-of-clojure-2016/blob/master/src/advent_of_clojure_2017/day13.clj
;; For a given level-size find the position for a given count
;; The count has to move down through the levels and then back up. Scanning
(defn scanner-position [count level-size]
  (let [level-size (dec level-size)
        division (/ count level-size)]
    (if (even? (long division))
      (rem count level-size)
      (- level-size (rem count level-size)))))

(defn run-part1 []
  (->> (load-data)
       (map (juxt #(scanner-position (key %) (val %)) identity))     ;; build ([0 [0 3]] [1 [1 2]] [2 [2 4]] ...
       (filter (comp zero? first))
       (map (comp #(apply * %) second))
       (apply +)))
;; 1612



;; Part 2
;; Delay entering the firewall so you don't get caught at all

;; @see https://github.com/bhauman/advent-of-clojure-2016/blob/master/src/advent_of_clojure_2017/day13.clj
(defn build-can-pass-at-time?-fn [data']
  (let [data-size (apply max (keys data'))]
    (fn [time-offset]
      (some
       (fn [[level range']]
         (zero?
          (scanner-position (+ level time-offset) range')))
       data'))))

(defn run-part2 []
  (->> (map (build-can-pass-at-time?-fn (load-data)) (range)) (take-while identity) count))
;; 3907994
