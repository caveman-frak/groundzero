plugins {
    `library-conventions`
    antlr
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
}

springBoot {
    mainClass.set("uk.co.bluegecko.parser.Main")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-listener", "-visitor", "-long-messages")
}