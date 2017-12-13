(ns advent.day12.plumber
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def url "./src/advent/day12/day12.txt")

(defn read-url [url]
  (with-open [rdr (clojure.java.io/reader url)]
    (doall (line-seq rdr))))

(defn parse-vals [s]
  (mapv #(Integer/parseInt %) (map str/trim (str/split (str/trim s) #","))))

(defn process-line [line]
  ;; 2 <-> 0, 3, 4
  (let [[a b] (str/split line #" <-> ")]
    (hash-map (Integer/parseInt a) (parse-vals b))))


(defn process-input []
  (loop [lines (read-url url)
         rtn {}]
    (if (empty? lines)
      rtn
      (recur (rest lines) (into rtn (process-line (first lines)))))))

(defn get-neighbors [m ids]
  (distinct (flatten (mapv #(get m %) ids))))

(defn new-neighbors [s ids]
  ;; return the ids that are not currently in s
  (set/difference (set ids) s))


(defn search-m [m ids]
  ;; starting with 0 search all the values
  ;; save all neighbors
  ;; (search-m m #{}[0])
  (loop [m m
         s (set ids)
         ids ids
         ]
    ;; (println s ids)
    (if (empty? ids)
      s
      (let [neighbors (get-neighbors m ids)]
        ;; for each of the neighbors
        ;; if it is not in s then add it to s and loop it it as an ids
        (let [ids (new-neighbors s neighbors)]
          (if (empty? ids)
            s
            (recur m (into s ids) (vec ids))))))))

        
        
        ;; (println neighbors)
        ;; ;; if no new neighbors return
        ;; ;; (set/difference a b) is a set containing things that are in a but not in b.
        ;; (if (empty? (set/difference s neighbors))
        ;;   s
        ;;   (recur m (into s neighbors) neighbors))))))

    
    


(defn run-part1 []
  (count (search-m (process-input) [0])))
;; 175



;; Part 2
;; How many groups in total

;; (run-part1) and remove it's group from m
;; run again
;; loop till there are no more in m

(defn remove-keys [m r]
  (apply dissoc m r))

(defn run-part2 []
  (loop [m (process-input)
         ids [0]
         cnt 0]
    (if (empty? m)
      cnt
      (let [r (search-m m ids)]
        ;; remove the keys r from m
        (let [m1 (remove-keys m r)
              ids2 [(first (keys m1))]]
              (recur m1 ids2 (inc cnt)))))))
  
;; 213         
