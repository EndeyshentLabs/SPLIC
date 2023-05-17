if exists('b:current_syntax')
  finish
endif

syntax keyword splTodo contained TODO FIXME XXX NOTE

syntax region splCommentLine start="}" end="$" display contains=splTodo

syntax match splOperators "+\|-\|*\|/\|!\|@:\|@\|#\|!#\||\|?:\|?"

syntax match splNumber "\v<\d+>"
syntax match splNumber "\v-<\d+>"

highlight default link splPrimitiveTypes Type
highlight default link splNumber         Number
highlight default link splCommentLine    Comment
highlight default link splOperators      Keyword

let b:current_syntax = 'splic'
