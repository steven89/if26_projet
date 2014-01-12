if26_projet
===========

projet if26 - application android monnaie virtuelle + webserver

# Client Android :

Utilisation de la lib support : http://developer.android.com/tools/support-library/setup.html

# WebService :
### User Database
```sql
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` text NOT NULL,
  `pass` text NOT NULL,
  `nom` text NOT NULL,
  `prenom` text NOT NULL,
  `tag` varchar(50) NOT NULL,
  `token` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag` (`tag`),
  UNIQUE KEY `email` (`email`(150))
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
```

### Webservice map

#####POST /user
```json
{
  "email" : "test@test.fr",
  "tag" : "johnDoe1337",
  "prenom" : "john",
  "nom"  : "doe",
  "pass" : "1234"
}
```

#####PUT /Login
```json
{
  "email" : "test@test.fr",
  "pass" : "1234"
}
```

#####PUT /Logout
```json
{
  "email" : "test@test.fr",
  "token" : "Afsdfh1356sfdEDZQS54sdADS15aa"
}
```

#####POST /Transaction
```json
{
  "email" : "test@test.fr",
  "token" : "Afsdfh1356sfdEDZQS54sdADS15aa",
  "to" : "johnDoe1337",
  "amount" : 10
}
```
