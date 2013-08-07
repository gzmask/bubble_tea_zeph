; Ray's zeph profile
; ~/.lein/profiles.clj: {:user {:plugins [[lein-exec "0.3.0"]]}}
; lein exec ~/.zeph/my-zeph.clj

(use '[leiningen.exec :only (deps)])
(deps '[[org.clojure/data.json "0.2.2"]])


(load-file "/Applications/Zephyros.app/Contents/Resources/libs/zephyros.clj")

(bind "h" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-left window))))

(bind "l" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-right window))))

(bind "k" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-up window))))

(bind "j" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-down window))))

(bind "k" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)] 
              (set-frame window {:x (:x frame)
                                :y (:y frame)
                                :w (:w frame)
                                :h (/ (:h frame) 2)}))))


(bind "j" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)]
              (set-frame window {:x (:x frame)
                                :y (+ (:y frame) (/ (:h frame) 2))
                                :w (:w frame)
                                :h (/ (:h frame) 2)}))))

(bind "h" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)]
              (set-frame window {:x (:x frame)
                                :y (:y frame)
                                :w (/ (:w frame) 2)
                                :h (:h frame)}))))


(bind "l" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)]
              (set-frame window {:x (+ (:x frame) (/ (:w frame) 2))
                                :y (:y frame)
                                :w (/ (:w frame) 2)
                                :h (:h frame)}))))

(bind "i" ["Cmd" "Shift"]
          (fn [] 
            (let [window (get-focused-window)
                  screen (get-screen-for-window window)
                  frame (screen-frame-without-dock-or-menu screen)]
              (alert (str 
                       "x:"
                       (:x frame) 
                       "y:"
                       (:y frame)
                       "w:"
                       (:w frame)
                       "h:"
                       (:h frame)) 3))))


@listen-for-callbacks ;; necessary when you use (bind) or (listen)
