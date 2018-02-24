Oi!
=============================================

## Overview

Oi! is an open-source application developed for Android that allows users to exchange short text message over an NDN infrastructure. Usually applications that work on top on NDN infrastructure use the pull communication paradigm in order to exchange data. However, based on some research work, it is possible to develop push communication applications on top of NDN, namely to establish a voice conversation over content-centric network. In the same way we aim to implement a mechanism to exchange data in Oi!, which makes use of the push communication model implemented in NDN-Opp.

With Oi!, users can maintain conversation sessions with different users at the same time. Since Oi! was developed on top of NDN-OPP, Oi! is able of exchanging messages even in condition with intermittent wireless connectivity. 

Oi! is specified based on a modular approach, so it is possible to add new components and allow integration with the NDN Forwarding Daemon (NFD) which is a network forwarder that implements NDN: however, the operation of Oi! on top of NFD is only possible if NDN Android implements a push communication model, as done by NDN-OPP.
