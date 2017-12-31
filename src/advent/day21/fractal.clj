(ns advent.day21.fractal
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

;; Inspired by https://github.com/bhauman/advent-of-clojure-2016/blob/master/src/advent_of_clojure_2017/day21.clj

(def url "src/advent/day21/day21.txt")

(def sample-rules "../.# => ##./#../...\n.#./..#/### => #..#/..../..../#..#")

;; ((\. \# \.) (\. \. \#) (\# \# \#))
(def starting-grid (map seq (clojure.string/split-lines ".#.\n..#\n###")))

(def transpose (partial apply map vector))

(def flip (partial map reverse))

(def rotate-right (comp flip transpose))

(defn all-rotations [rule-key]
  (let [rotations (take 4 (iterate rotate-right (map seq rule-key)))]
    (concat rotations (map flip rotations))))

(defn expand-rule [rules [rule-key rule-val]]
  (->> (repeat (map seq rule-val))
       (map vector (all-rotations rule-key))
       (into {})
       (merge rules)))

(defn load-rules []
  (->> url
       slurp
       string/split-lines
       (map #(string/split % #" => "))
       (map (partial map #(string/split % #"/")))
       (reduce expand-rule {})))

(defn split-grid [grid n]
  (map #(map transpose (partition n (transpose %)))
       (partition n grid)))

(defn normalize [broken-out]
  (mapcat #(map flatten (transpose %)) broken-out))

(defn grid-size [grid]
  (count (first grid)))

(defn num-on-pixels [grid]
  (count (filter #{\#} (flatten grid))))

(defn count-at-depth [rules depth grid]
  (if (zero? depth)
    (num-on-pixels grid)
    (condp = (grid-size grid)
      2 (count-at-depth rules (dec depth) (rules grid))
      3 (count-at-depth rules (dec depth) (rules grid))
      4 (count-at-depth rules (dec depth) (->> (split-grid grid 2) 
                                               (map (partial map rules))
                                               normalize))
      6 (->> (split-grid grid 2)
             (mapcat (partial map rules))
             (map #(count-at-depth rules (dec depth) %))
             (reduce +)))))

(defn run-part1 []
  (count-at-depth (load-rules) 5 starting-grid))
;; 117

(defn run-part2 []
  (count-at-depth (load-rules) 18 starting-grid))
;; 2026963
