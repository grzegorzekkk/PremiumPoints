# PremiumPoints
[Minecraft Plugin] Premium server currency system.

This plugin is PlayerPoints extension providing currency payment history, leaderboard, SMS purchase method.

## Requirements:
* PlayerPoints https://dev.bukkit.org/projects/playerpoints
(Plugin tested on version 2.1.4)
* Spigot 1.12 server
* Mysql database
* email account

## Installation:
1. Drop PremiumPoints.jar to your plugins directory
2. Start server
3. Edit config.yml and messages.yml in plugin's directory to your needs.
4. Restart server or reload config by using reload command.

## Commands:
* /pp - main command of plugin.
* /pp add <player> <pp_value> <secret> - Adminnistrator command that adds premiumpoints to player's account
* /pp reload - Administrator command that reloads configuration.
* /pp givepsc - Administrator command that transfers premium points to player.
* /pp sms - Command for sms premium points payment.
* /pp history - Command to check purchases history.
* /pp top - description: Command to check top buyers of the month.
* /pp info - Main plugin command.
* /pp psc - Command for psc premium points payment.
* /pp collect - Command to redeem pp bought by Paysafecard.
* /pp pay - Command for sending pp to other players.

## Permissions:
  * pp.add:
    default: op
  * pp.reload:
    default: op
  * pp.givepsc:
    default: op
  * pp.sms:
    default: true
  * pp.history:
    default: true
  * pp.top:
    default: true
  * pp.info:
    default: true
  * pp.psc:
    default: true
  * pp.collect:
    default: true
  * pp.pay:
    default: true

## Configuration file:
```
database:
  host: 'localhost'
  port: 3306
  database_name: 'name'
  username: 'username'
  password: 'password'
email:
  smtp: 'smtp.xx.com'
  port: 465
  username: 'email'
  password: 'password'
  receiver: 'send_to_email'
pp_multiplier: 1.0
secret: 'secret'
provider:
  url: 'http://sms.cashbill.pl/backcode_check.php'
  resp_valid: 'OK'
  services:
    '30':
      points: 30
      number: 73480
      prefix: 'prefix'
      price: 3.69
    '60':
      points: 60
      number: 76480
      prefix: 'prefix'
      price: 7.38
    '90':
      points: 90
      number: 79480
      prefix: 'prefix'
      price: 11.07
    '140':
      points: 140
      number: 91400
      prefix: 'prefix'
      price: 17.22
    '190':
      points: 190
      number: 91900
      prefix: 'prefix'
      price: 23.37
```

## Messages file:
```
header: '============ [PP (premium points)] ============'
no_permission: '&4You do not have permission to use this command!'
invalid_arg_order: '&cEntered arguments are in invalid order.'
invalid_arg_amount: '&cEntered too less or too much arguments.'
player_needed: '&cThis commands needs to be executed by a player!'
config_reloaded: '&9Configuration file and messages file has been succesfully reloaded!'
command_not_found: '&4This command does not exist!'
sms_payment_succ: '&aSuccessfully bought %pp% pp!'
sms_payment_fail: '&cEntered code is invalid or has been used before!'
sms_payment_broadcast: '&3Player &c%player%&3 has bought &c%pp%&3 pp by using command &c/pp sms'
add_received_succ: '&3You have received &c%pp%&3 pp.'
add_send_succ: '&3Successfully added %pp% pp to %player% account.'
history_empty: '&cYou havent bought any premium points yet.'
top_empty: '&cNobody bought premium points in this month yet.'
collect: '&3You have some pp bought by Paysafecard to collect. Type &c/pp collect&3 to redeem them'
collect_succ: '&aSuccessfully redeemed pp!'
collect_fail: '&cYou have no pp to collect!'
history: '&3Last 5 transactions:'
history_each: '&3- &c%pp%&3 pp, type: &c%type%&3, date &c%date%'
pay: '&3To send somebody premium points, type &c/pp pay <player> <amount>'
pay_succ: '&aSuccessfully send %pp% pp to player %player%'
pay_fail: '&cInvalid syntax of command. Please use &9/pp pay <player> <amount>'
pay_not_enough: '&cYou have not enough pp to send this amount to other player.'
pay_received: '&3You have received &c%pp%&3 pp from player &c%player%&3.'
top: '&3Top 5 pp buyers of the month:'
top_each: '&3%id%. &c%player%&3 has bought &c%pp%&3 pp.'
info: |-
  &3Welcome &c%playername%&3. You have &c%player_pp%&3 pp.
  &3Available commands:
  &c/pp&3 - main plugin page
  &c/pp sms&3 - command to buy premium points
  &c/pp psc&3 - command to buy pp by PSC
  &c/pp history&3 - check your last purchases
  &c/pp top&3 - show top 5 buyers of this month
psc: |-
  &3If you want to buy pp by PaySafeCard
  &3type: &c/pp psc <16 digits code>
  &3i.e &c/pp psc 1234123412341234
  &c1&3zl = &c100&3pp
  &3Remember that all cash stored on PIN code
  &3will be taken
psc_invalid: '&cThe entered PaySafeCard code is not 16 characters long or contains letters.'
psc_valid: '&aSuccessfully send code to server admin. Now wait for validation.'
sms: |-
  &3You can buy &c30&3, &c60&3, &c90&3, &c140&3 or &c190&3 pp
  &3If you want, type: &c/pp sms <value>
  &3i.e &c/pp sms 60
sms_choice: |-
  &3If you want to buy %pp% pp
  &3Send sms of message &c%message%&3 on &c%number%
  &3Cost of sms: &c%cost% &3with VAT.
  &3Then provide received code by typing:
  &c/pp sms %pp% <code>
  &3i.e &c/pp sms %pp% ABCDEFGH

```

Enjoy!
