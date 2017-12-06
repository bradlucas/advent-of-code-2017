(ns advent.day4.passphrase)


;; input
;; http://adventofcode.com/2017/day/4/input
(def url "/Users/Brad/work/advent-of-code-2017/src/advent//day4/day4.txt")


;; valid-passphrase
;; must contain no duplicate words

;; @see https://clojuredocs.org/clojure.core/frequencies
(defn valid-passphrase [s]
  (= 1 (apply max (vals (frequencies (clojure.string/split s #" "))))))

(defn run1 [url]
  ;; read url into lines
  (with-open [rdr (clojure.java.io/reader url)]
    (count (filter true? (map valid-passphrase (line-seq rdr))))
    ))
;; 386

(defn run-part1 []
  (run1 url))


;; Part 2

(defn sort-word-chars [word]
  (clojure.string/join (sort word)))

(defn valid-passphrase2 [s]
  ;; to be valid there needs to be no anagrams
  ;; are there any two words with the same set of characters
  ;; sort each word
  ;; if any duplicates you have anagrams
  (= 1 (apply max (vals (frequencies (map sort-word-chars (clojure.string/split s #" ")))))))

(defn run2 [url]
  ;; read url into lines
  (with-open [rdr (clojure.java.io/reader url)]
    (count (filter true? (map valid-passphrase2 (line-seq rdr))))
    ))
;; 208

(defn run-part2 []
  (run2 url))
