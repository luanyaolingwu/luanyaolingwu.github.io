package moe.fuqiuluo.api

object WrongKeyError: RuntimeException("Wrong API key.")

object BlackListError: RuntimeException("Blacklist uin.")

object WrongQuitTokenError: RuntimeException("Wrong quit token.")