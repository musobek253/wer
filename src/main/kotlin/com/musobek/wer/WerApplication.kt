package com.musobek.wer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = arrayOf("com.musobek.wer.auth.repo"))
@EntityScan(basePackages = arrayOf("com.musobek.wer.auth.entity"))
@SpringBootApplication
class WerApplication

fun main(args: Array<String>) {
    runApplication<WerApplication>(*args)
}
