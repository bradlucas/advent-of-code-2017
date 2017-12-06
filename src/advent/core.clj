(ns advent.core
  (:require [advent.day1.captcha :as day1-part1]
            [advent.day1.captcha2 :as day1-part2])
  (:gen-class))


(defn -main [& args]
  (println "Advent of Code 2017")
  (println "--------------------------------------------------")
  (println (format "Day 1 / Part 1 : %d" (day1-part1/run)))
  (println (format "Day 1 / Part 2 : %d" (day1-part2/run)))

  )
