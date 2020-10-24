 
Per fare una nuova release e pubblicare su maven central

# mvn release:prepare
# mvn release:perform -P sing

Attenzione ad avere le chiave nella HOME/.gnugpg che hai distribuito in precedenza ai key server.

 https://central.sonatype.org/pages/apache-maven.html
 https://central.sonatype.org/pages/working-with-pgp-signatures.html