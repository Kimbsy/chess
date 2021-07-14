(ns chess.common
  (:require [clojure.string :as s]))

(def ANSI_RESET "\u001B[0m")
(def ANSI_BLACK "\u001B[30m")
(def ANSI_RED "\u001B[31m")
(def ANSI_GREEN "\u001B[32m")
(def ANSI_YELLOW "\u001B[33m")
(def ANSI_BLUE "\u001B[34m")
(def ANSI_PURPLE "\u001B[35m")
(def ANSI_CYAN "\u001B[36m")
(def ANSI_WHITE "\u001B[37m")

(def ANSI_BLACK_BG "\u001B[40m")
(def ANSI_RED_BG "\u001B[41m")
(def ANSI_GREEN_BG "\u001B[42m")
(def ANSI_YELLOW_BG "\u001B[43m")
(def ANSI_BLUE_BG "\u001B[44m")
(def ANSI_PURPLE_BG "\u001B[45m")
(def ANSI_CYAN_BG "\u001B[46m")
(def ANSI_WHITE_BG "\u001B[47m")

(def ANSI_NC "\033[0m")

(def initial-fen   "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
(def initial-fen+1 "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1")
(def initial-fen+2 "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2")
(def initial-fen+3 "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")

(def initial-board
  {:state [(vec "rnbqkbnr")
           (vec "pppppppp")
           (vec "        ")
           (vec "        ")
           (vec "        ")
           (vec "        ")
           (vec "PPPPPPPP")
           (vec "RNBQKBNR")]
   :details "w KQkq - 0 1"})

(defn fen-rank->rank
  [fen-rank]
  (-> fen-rank
      (s/replace #"1" " ")
      (s/replace #"2" "  ")
      (s/replace #"3" "   ")
      (s/replace #"4" "    ")
      (s/replace #"5" "     ")
      (s/replace #"6" "      ")
      (s/replace #"7" "       ")
      (s/replace #"8" "        ")
      vec))

(defn rank->fen-rank
  [rank]
  (-> (apply str rank)
      (s/replace #"        " "8")
      (s/replace #"       " "7")
      (s/replace #"      " "6")
      (s/replace #"     " "5")
      (s/replace #"    " "4")
      (s/replace #"   " "3")
      (s/replace #"  " "2")
      (s/replace #" " "1")))

(defn fen->board
  [fen]
  (let [split-fen (s/split fen #"[ \/]")
        fen-ranks (take 8 split-fen)
        details   (drop 8 split-fen)]
    {:state   (mapv fen-rank->rank fen-ranks)
     :details details}))

(defn board->fen
  [{:keys [state details]}]
  (str
   (->> state
        (map rank->fen-rank)
        (s/join "/"))
   " "
   details))
