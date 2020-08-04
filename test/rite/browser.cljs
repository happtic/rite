(ns rite.browser
  (:require  [doo.runner :refer-macros [doo-tests]]
             [rite.core-test]))

(doo-tests 'rite.core-test)
