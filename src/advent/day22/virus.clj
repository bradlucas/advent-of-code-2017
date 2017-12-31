(ns advent.day22.virus
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

;; @see https://github.com/bhauman/advent-of-clojure-2016/blob/master/src/advent_of_clojure_2017/day22.clj

(def input-data
  (->> "src/advent/day22/day22.txt"
       io/reader
       line-seq
       vec))
       
(def test-data ["..#" "#.." "..."])

;; First number is up/down while second number is left/right
;; (get (get g first-num) second-num)
(def directions [[-1 0]   ;; up
                 [0 1]    ;; right
                 [1 0]    ;; down
                 [0 -1]]) ;; left

;; If current node is infected, it turns right
;; Otherwise it turns left

;; If the current node is clean, it becomes infected
;; Otherwise, it becomes cleaned

;; The virus carrier moves forward one node in the direction
;; it is facing

;; Clean nodes are represented as a .
;; Infected nodes are shown as a #

;; The carrier begins in the middle of the map facing up

;; |------------+-------------+------------+--------------|
;; | Node State | Action      | Turn       |              |
;; |------------+-------------+------------+--------------|
;; | Clean      | Infect Node | Turn Left  | Move Forward |
;; | Infected   | Clean Node  | Turn Right | Move Forward |
;; |------------+-------------+------------+--------------|


;; Part 1
;; After 10,000 bursts of activity, how many bursts cause a node to become infected

(defn center-position [data]
  (let [x (int (/ (count data) 2))]
    [x x]))

(defn build-board [data]
  ;; data should be square
  {:board (into {}
                (for [y (range (count data))
                      x (range (count data))]
                  [[y x] (str (get-in data [y x]))]))
                      
   ;; initial position is in the center
   :position (center-position data)

   ;; intial direction is up
   :direction 0   ;; [-1 0]
   })

(def turn-right (comp #(mod % 4) inc))
(def turn-left (comp #(mod % 4) dec))

(defn turn [{:keys [board position] :as state}]
  ;; If clean turn left else turn right
  (->> (if (= "#" (get board position)) turn-right turn-left)
       (update state :direction)))
    
(defn infect [{:keys [board position] :as state}]
  ;; If infected then clean else infect
  (if (= "#" (get board position))
    (update state :board assoc position ".")   ;; clean it
    (-> state
        (update :board assoc position "#")
        (update :infect-count (fnil inc 0)))))

(defn move [{:keys [direction] :as state}]
  ;; move forward
  (update state :position #(mapv + % (get directions direction))))

(defn run-part1 []
  (let [board (build-board input-data)]
    (-> 
     (iterate
      (comp move infect turn)
      board)
     (nth 10000)
     :infect-count)))
;; 5399

;; Part 2

;; States
;; Clean nodes become weakened.
;; Weakened nodes become infected.
;; Infected nodes become flagged.
;; Flagged nodes become clean.

(def states 
  {nil "W"
   "." "W"
   "W" "#"
   "#" "F"
   "F" "."})

(defn turn-2 [{:keys [board position] :as state}]
  (condp = (get board position ".")
    "." (update state :direction turn-left)
    "#" (update state :direction turn-right)
    "W" state
    "F" (update state :direction (comp turn-right turn-right))))

(defn update-node [{:keys [board position] :as state}]
  (cond-> state
    (= "W" (board position)) (update :infect-count (fnil inc 0))
    :else (update-in [:board position] states)))

(defn run-part2 []
  (let [board (build-board input-data)]
    (-> 
     (iterate
      (comp move update-node turn-2)
      board)
     (nth 10000000)
     :infect-count)))
;; 2511776

