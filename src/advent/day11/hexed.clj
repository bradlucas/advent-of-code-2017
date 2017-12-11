(ns advent.day11.hexed)

(def url "./src/advent/day11/day11.txt")

(defn process-str [s]
  (map keyword (clojure.string/split s '#",")))

(defn load-data [url]
  (clojure.string/trim (slurp url)))
  
(defn command [c]
  (case c
    :n [0 1]
    :ne [1 1]
    :se [1 -1]
    :s [0 -1]
    :sw [-1 -1]
    :nw [-1 1]
    nil))
  
(defn solve [s]
  (reduce
   (fn [[a b] c]
     (let [[c d] (command c)]
       [(+ a c) (+ b d)]
       ))
   [0 0]
   (process-str s)))

(defn run-part1 []
  (let [rtn (solve (load-data url))]
    (apply max rtn)))
;; 696


;; Part 2
;; What is the max distance ever reached

(defn abs [n] (if neg? n) (- n) n)

(defn find-max [a b m]
  (max (abs m) (abs a) (abs b)))

(defn solve2 [s]
  (reduce
   (fn [[a b m] c]
     (let [[c d] (command c)
           a' (+ a c)
           b' (+ b d)
           m' (find-max a' b' m)]
       [a' b' m']
       ))
   [0 0 0]
   (process-str s)))

(defn run-part2 []
  (let [rtn (solve2 (load-data url))]
    (nth rtn 2)))
;; 1461
 
