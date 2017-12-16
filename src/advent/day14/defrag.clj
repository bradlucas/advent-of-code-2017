(ns advent.day14.defrag
  (:require [advent.day10.knothash :as hash]))

(def size 128)

(def test-str "flqrgnkx")

(def input-str "uugsqrei")

(defn knot-hash-hex [s]
  (hash/part2 s))

(comment
  (= (knot-hash-hex "AoC 2017") "33efeb34ea91902bb2f59c9920caa6cd")
  (= (knot-hash-hex  "flqrgnkx-0") "d4f76bdcbf838f8416ccfa8bc6d1f9e6"))


;; char->int
;; @see https://github.com/dandorman/advent-of-code-2017/blob/master/src/advent_of_code_2017/day14.clj
(def char->int
  ;; turn a hex number into an integer by building a hex 'map'
  ;; 
  (zipmap
   ;; keys
   (map char
        (concat
         (range (int \0) (+ (int \0) 10))    ;; 48 49 50 51 52 53 54 55 56 57
         (range (int \a) (+ (int \a) 6))))
                          
   ;; values
   (range 16)))

(defn build-row [s idx]
  (->> (str s \- idx)
       knot-hash-hex
       (map char->int)
       (mapcat #(Integer/toBinaryString %))))

(defn num-row-blocks [l]
  (->> l
       (filter #{\1})
       (into [])
       count))
       
(defn process [s size]
  (reduce + (map #(num-row-blocks (build-row s %)) (range size))))

(defn run-part1 []
  (process input-str 128))
;; 8194


;; Part 2
;; @see https://github.com/dandorman/advent-of-code-2017/blob/master/src/advent_of_code_2017/day14.clj

;; \a -> "1010"
(defn char->binary-string [c]
  ;; pad out to 4 character with leading 0
  (let [bin (Integer/toBinaryString (char->int c))]
    ;; prefix is the number of 0s to total bin to 4 characters
    (str (apply str (repeat (- 4 (count bin)) \0)) bin)))

(defn build-row [input idx]
  (->> (str input \- idx)
       knot-hash-hex
       (mapcat char->binary-string)
       (into [])))

(defn build-grid [input size]
  (->> (range size)
       (map #(build-row input %))
       (into [])))

(defn build-region [grid seen [row col]]
  (let [neighbors [[(dec row) col]
                   [(inc row) col]
                   [row (dec col)]
                   [row (inc col)]]]
    (reduce (fn [seen neighbor]
              (if-let [v (get-in grid neighbor)]
                (if (and (not (contains? seen neighbor))  ;; not in seen
                         (= \1 v))                        ;; is \1
                  (build-region grid (conj seen neighbor) neighbor)
                  seen)
                seen))
            seen
            neighbors)))

(defn regions [grid]
  (let [height (count grid)
        width  (count (first grid))]
    (loop [seen #{}
           regions 0
           row 0
           col 0]
      (cond
        (<= height row) regions
        (<= width col) (recur seen regions (inc row) 0)
        :else (if (and (not (contains? seen [row col]))   ;; not in seen
                       (= \1 (get-in grid [row col])))    ;; is \1
                (recur (build-region grid seen [row col])
                       (inc regions)
                       row
                       (inc col))
                (recur seen regions row (inc col)))))))

(defn run-part2 []
  (let [input input-str
        size 128]
    (let [grid (build-grid input size)]
      (regions grid))))
;; 1141

