if26_projet
===========

projet if26 - application android monnaie virtuelle + webserver

# WebService :
création de la DB :
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
