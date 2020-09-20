# uri-friend
 A friendly URI shortener.

 ## Thinking alound...

let's take a URI like

`https://labs.rs/en/the-human-fabric-of-the-facebook-pyramid?param=test`


In particular here we have a URL. Broken down that is:

**Scheme:** `https`<br>
**Domain:** `labs.rs`<br>
**Port:** `443` *(altough not explicit)*<br>
**Path:** `en/the-human-fabric-of-the-facebook-pyramid`<br>
**Query:** `?param=test`<br>

It's a little long, let's shorten it.

Possible options

`https://example.com/u/1` could just persist an id which maps to the real uri?

`https://example.com/u/?s=2&d=1&p=2&pa=1&q=1` could do something where we map each part of the URI to an ID. This saves storing repetivie information for multiple URI's.

We actually know we we always have a Scheme, Domain and Port, even if the port is empty, so they could just be the first parts.

`https://example.com/u/scheme_id/domain_id/port_id?rel=relative_path_and_query_id`

`https://example.com/u/2/1/0?rel=1`

That looks a little better and you can see the breakdown of the required id's and optionaly the path and query, but it's still not very short. 

I coudld seperate those ID's using a letter, can be any letter I think it's not important? This just let's me identify where one ID ends and another begins. So instead of:

`https://example.com/u/2/1/0?rel=1`

I could have something like:

`https://example.com/u/2w1a0s1`

Assuming that most URIs and probably just going to be URL's which similar schemes and ports, this is less information to store in a DB. The downside here is the domain count could grow. once we hit one million domains our url would look soemthing like:

`https://example.com/u/2w1000000a0s1`

and with one million different paths that would looks something like:

`https://example.com/u/2w1000000a0s1000000`

I think there is some further encoding we can do to reduce those numbers, btu first let's work on creating this.


## Running

### Create a network

```
docker network create urifriend-net
```

### Database

Get your database running. Make sure you have somewhere to store your volumes e.g.

```
mkdir -p $HOME/docker/volumes/postgres
```

Spin up postgres

```
docker run --rm --name pg -e POSTGRES_PASSWORD=test -d -p 5432:5432 --net urifriend-net -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
```

### Uri friend service

Spin up the uri shortening service

```
docker run --rm -d -p 8080:80 --name uri-friend --net urifriend-net tomco/uri-friend
```

