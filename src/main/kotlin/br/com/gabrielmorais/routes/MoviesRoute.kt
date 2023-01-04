package br.com.gabrielmorais.routes

import br.com.gabrielmorais.models.Message
import br.com.gabrielmorais.models.Movie
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

val movies = mutableListOf<Movie>()
fun Application.moviesRoute() = routing {
  addMovies()
  getMovies()
  getMovieByName()
  insertMovie()
  updateMovie()
  deleteMovie()
}

fun Route.getMovies() = get("/movies") {
  val movieList = movies.toList()
  call.respond(status = HttpStatusCode.OK, movieList)
}

fun Route.getMovieByName() = get("/movies/{name}") {
  val movieName = call.parameters["name"]
  val movie = movies.find { it.name?.lowercase() == movieName?.lowercase() }
  movie?.let {
    call.respond(status = HttpStatusCode.OK, movie)
  }
  call.respond(status = HttpStatusCode.NotFound, Message("Filme não encontrado"))
}

fun Route.insertMovie() = post("/movies") {
  try {
    val movie = call.receive<Movie>()
    if (movie.name == null) {
      call.respond(status = HttpStatusCode.BadRequest, Message("Nome inválido"))
    }
    if (movie.category == null) {
      call.respond(status = HttpStatusCode.BadRequest, Message("Categoria inválida"))
    }
    movies.add(movie)
    call.respond(status = HttpStatusCode.Created, Message("Filme adicionado com sucesso"))
  } catch (e: Exception) {
    println("Erro: ${e.message}")
  }
}

fun Route.updateMovie() = put("/movies") {
  val movie = call.receive<Movie>()
  if (movie.name == null) {
    call.respond(status = HttpStatusCode.BadRequest, Message("Nome inválido"))
  }
  if (movie.category == null) {
    call.respond(status = HttpStatusCode.BadRequest, Message("Categoria inválida"))
  }

  movies.find { it.name == movie.name }?.apply {
    category = movie.category
    call.respond(status = HttpStatusCode.OK, Message("Filme atualizado com sucesso"))
  }

  call.respond(status = HttpStatusCode.NotFound, Message("Filme não encontrado"))
}

fun Route.deleteMovie() = delete("/movies/{name}") {
  val movieName = call.parameters["name"]
  val result = movies.removeIf {
    it.name?.lowercase() == movieName?.lowercase()
  }

  if (result) {
    call.respond(status = HttpStatusCode.OK, Message("Filme removido com sucesso"))
  }
  call.respond(status = HttpStatusCode.NotFound, Message("Não foi possível remover o filme"))

}

fun addMovies() {
  val avengers = Movie("avengers", "action")
  val starwars = Movie("Star Wars", "Science Fiction")
  val avatar = Movie("Avatar", "Science Fiction")
  movies.addAll(
    listOf(
      avengers,
      starwars,
      avatar
    )
  )
}