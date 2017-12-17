(ns advent.core
  (:require [advent.day1.captcha :as day1-part1]
            [advent.day1.captcha2 :as day1-part2]
            [advent.day2.checksum :as day2]
            [advent.day3.spiral :as day3]
            [advent.day4.passphrase :as day4]
            [advent.day5.trampoline :as day5]
            [advent.day6.reallocation :as day6]
            [advent.day7.circus :as day7]
            [advent.day8.registers :as day8]
            [advent.day9.stream :as day9]
            [advent.day10.knothash :as day10]
            [advent.day11.hexed :as day11]
            [advent.day12.plumber :as day12]
            [advent.day13.scanner :as day13]
            [advent.day14.defrag :as day14]
            [advent.day15.dueling :as day15]
            [advent.day15.dueling2 :as day152]
            [advent.day16.promenade :as day16]
            )
  (:gen-class))


(defn -main [& args]
  (println "Advent of Code 2017")
  (println "--------------------------------------------------")
  (println (format "Day  1 / Part 1 : %d" (day1-part1/run)))
  (println (format "Day  1 / Part 2 : %d" (day1-part2/run)))

  (println (format "Day  2 / Part 1 : %d" (day2/run-part1)))
  (println (format "Day  2 / Part 2 : %d" (day2/run-part2)))

  (println (format "Day  3 / Part 1 : %d" (day3/run-part1)))
  (println (format "Day  3 / Part 2 : %d" (day3/run-part2)))

  (println (format "Day  4 / Part 1 : %d" (day4/run-part1)))
  (println (format "Day  4 / Part 2 : %d" (day4/run-part2)))

  (println (format "Day  5 / Part 1 : %d" (day5/run-part1)))
  (println (format "Day  5 / Part 2 : %d" (day5/run-part2))) 

  (println (format "Day  6 / Part 1 : %d" (day6/run-part1)))
  (println (format "Day  6 / Part 2 : %d" (day6/run-part2)))
  
  (println (format "Day  7 / Part 1 : %s" (day7/run-part1)))
  (println (format "Day  7 / Part 2 : %d" (day7/run-part2)))

  (println (format "Day  8 / Part 1 : %d" (day8/run-part1)))
  (println (format "Day  8 / Part 2 : %d" (day8/run-part2)))

  (println (format "Day  9 / Part 1 : %d" (day9/run-part1)))
  (println (format "Day  9 / Part 2 : %d" (day9/run-part2)))

  (println (format "Day 10 / Part 1 : %d" (day10/run-part1)))
  (println (format "Day 10 / Part 2 : %s" (day10/run-part2)))
  
  (println (format "Day 11 / Part 1 : %d" (day11/run-part1)))
  (println (format "Day 11 / Part 2 : %s" (day11/run-part2)))

  (println (format "Day 13 / Part 1 : %d" (day13/run-part1)))
  (println (format "Day 13 / Part 2 : %d" (day13/run-part2)))

  (println (format "Day 14 / Part 1 : %d" (day14/run-part1)))
  (println (format "Day 14 / Part 2 : %d" (day14/run-part2)))

  (println (format "Day 15 / Part 1 : %d" (day15/run-part1)))
  (println (format "Day 15 / Part 2 : %d" (day152/run-part2)))

  (println (format "Day 16 / Part 1 : %s" (day16/run-part1)))
  (println (format "Day 16 / Part 2 : %s" (day16/run-part2)))
  
  )
